package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getBookings(long userId, String state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.toUpperCase(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingsByOwner(long userId, String state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.toUpperCase(),
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> addBooking(long userId, BookingDtoIn bookingDtoIn) {
        LocalDateTime now = LocalDateTime.now().withNano(0).minusSeconds(2);

        if (bookingDtoIn.getItemId() == null || bookingDtoIn.getStart().isBefore(now)
                || bookingDtoIn.getEnd().isBefore(now)) {
            throw new ValidationException("Ошибка Валидации");
        }
        return post("", userId, bookingDtoIn);
    }

    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> setApproveBookingByOwner(Long userId, Long bookingId, Boolean approve) {
        return patch("/" + bookingId + "?approved=" + approve, userId, approve);
    }
}