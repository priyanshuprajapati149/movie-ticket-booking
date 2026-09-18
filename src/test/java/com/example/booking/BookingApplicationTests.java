package com.example.booking;

import com.example.booking.entity.Seat;
import com.example.booking.repository.SeatRepository;
import com.example.booking.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BookingApplicationTests {

	@Autowired
	private BookingService bookingService;

	@Autowired
	private SeatRepository seatRepository;

	@Test
	void testConcurrentBooking_solvesRaceCondition() throws InterruptedException {
		// We have Seat A1 with ID 1
		Long seatId = 1L;
		
		int threadCount = 2;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(1);
		CountDownLatch doneLatch = new CountDownLatch(threadCount);

		AtomicInteger successfulBookings = new AtomicInteger(0);
		AtomicInteger failedBookings = new AtomicInteger(0);

		Runnable bookTaskAlice = () -> {
			try {
				latch.await(); // wait for start signal
				bookingService.bookSeat(seatId, "alice");
				successfulBookings.incrementAndGet();
			} catch (Exception e) {
				failedBookings.incrementAndGet();
			} finally {
				doneLatch.countDown();
			}
		};

		Runnable bookTaskBob = () -> {
			try {
				latch.await(); // wait for start signal
				bookingService.bookSeat(seatId, "bob");
				successfulBookings.incrementAndGet();
			} catch (Exception e) {
				failedBookings.incrementAndGet();
			} finally {
				doneLatch.countDown();
			}
		};

		executorService.submit(bookTaskAlice);
		executorService.submit(bookTaskBob);

		// start threads at exactly the same time
		latch.countDown();
		
		// wait for threads to finish
		doneLatch.await();

		// Since we use pessimistic locking, only ONE booking should succeed
		assertEquals(1, successfulBookings.get(), "Only one booking should be successful");
		assertEquals(1, failedBookings.get(), "One booking should fail due to race condition lock");

		Seat seat = seatRepository.findById(seatId).get();
		assertEquals(Seat.SeatStatus.BOOKED, seat.getStatus());
	}
}
