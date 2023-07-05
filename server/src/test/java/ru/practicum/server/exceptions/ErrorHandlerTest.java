package ru.practicum.server.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorHandlerTest {
    ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void userNotFoundTest() {
        ErrorResponse errorResponse = errorHandler.userNotFound(new UserNotFoundException(111L));
        assertEquals(errorResponse.getError(), "User with id: 111 does not exist.");
    }

    @Test
    void itemNotFound() {
        ErrorResponse errorResponse = errorHandler.itemNotFound(new ItemNotFoundException(111L));
        assertEquals(errorResponse.getError(), "Item with id: 111 does not exist.");
    }

    @Test
    void notOwnerOrNotBooker() {
        ErrorResponse errorResponse = errorHandler.notOwnerOrNotBooker(new NotOwnerOrNotBookerException("test"));
        assertEquals(errorResponse.getError(), "test");
    }

    @Test
    void itemNotAvailable() {
        ErrorResponse errorResponse = errorHandler.itemNotAvailable(new ItemNotAvailableException(111));
        assertEquals(errorResponse.getError(), "Item with id: 111 is not available.");
    }

    @Test
    void endBeforeOrEqualStart() {
        ErrorResponse errorResponse = errorHandler.endBeforeOrEqualStart(new EndBeforeOrEqualStartException());
        assertEquals(errorResponse.getError(), "End date and time can't be before start date and time.");
    }

    @Test
    void bookingOfOwnItem() {
        ErrorResponse errorResponse = errorHandler.bookingOfOwnItem(new BookingOfOwnItemException());
        assertEquals(errorResponse.getError(), "You can't book your own item.");
    }

    @Test
    void bookingNotFound() {
        ErrorResponse errorResponse = errorHandler.bookingNotFound(new BookingNotFoundException(111));
        assertEquals(errorResponse.getError(), "Booking with id: 111 does not exist.");
    }

    @Test
    void bookingAlreadyApproved() {
        ErrorResponse errorResponse = errorHandler.bookingAlreadyApproved(new BookingAlreadyApprovedException(111));
        assertEquals(errorResponse.getError(), "Booking with id: 111 is already approved.");
    }

    @Test
    void unknownBookingState() {
        ErrorResponse errorResponse = errorHandler.unknownBookingState(new UnknownBookingStateException("STATE"));
        assertEquals(errorResponse.getError(), "STATE");
    }

    @Test
    void itemWasNotBookedEarlier() {
        ErrorResponse errorResponse = errorHandler.itemWasNotBookedEarlier(new ItemWasNotBookedEarlierException());
        assertEquals(errorResponse.getError(), "This item was not booked by you earlier.");
    }

    @Test
    void pageableParametersAreInvalid() {
        ErrorResponse errorResponse = errorHandler.pageableParametersAreInvalid(new PageableParametersAreInvalidException("test"));
        assertEquals(errorResponse.getError(), "test");
    }

    @Test
    void requestNotFound() {
        ErrorResponse errorResponse = errorHandler.requestNotFound(new RequestNotFoundException(111));
        assertEquals(errorResponse.getError(), "Request with id: 111 was not found.");
    }
}