package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Test
    void createRequest_shouldSuccessfullyCreateRequest() {
        Long userId = 1L;
        NewRequest newRequest = new NewRequest("Description", userId);

        User user = new User(userId, "john.doe@mail.com", "John Doe");
        ItemRequest savedRequest = new ItemRequest(1L, "Description", user, LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.save(any(ItemRequest.class))).thenReturn(savedRequest);

        var result = itemRequestService.create(userId, newRequest);

        assertNotNull(result);

        verify(userRepository).findById(userId);
        verify(requestRepository).save(any(ItemRequest.class));
    }
}
