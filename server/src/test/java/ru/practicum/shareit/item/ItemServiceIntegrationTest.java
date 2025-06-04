package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.enums.Statuses;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.NewCommentRequest;
import ru.practicum.shareit.item.dto.AdvancedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "spring.datasource.username=shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ItemServiceIntegrationTest {
    private final EntityManager em;
    private final ItemService itemService;

    private void createUserInDb() {
        Query userQuery = em.createNativeQuery("INSERT INTO Users (id, name, email) " +
                "VALUES (:id , :name , :email);");
        userQuery.setParameter("id", "1");
        userQuery.setParameter("name", "Ivan Ivanov");
        userQuery.setParameter("email", "ivan@email");
        userQuery.executeUpdate();
    }

    private void createItemInDb() {
        Query itemQuery = em.createNativeQuery("INSERT INTO Items (id, name, description, available, owner_id, request_id) " +
                "VALUES (:id , :name , :description , :available , :owner_id , :request_id);");
        itemQuery.setParameter("id", "1");
        itemQuery.setParameter("name", "name");
        itemQuery.setParameter("description", "description");
        itemQuery.setParameter("available", Boolean.TRUE);
        itemQuery.setParameter("owner_id", "1");
        itemQuery.setParameter("request_id", "1");
        itemQuery.executeUpdate();
    }

    private void createLastBookingInDb() {
        Query lastBookingQuery = em.createNativeQuery("INSERT INTO Bookings (id, start_date, end_date, item_id, status, booker_id) " +
                "VALUES (:id , :startDate , :endDate , :itemId , :status , :bookerId);");
        lastBookingQuery.setParameter("id", "1");
        lastBookingQuery.setParameter("startDate", LocalDateTime.now().minusDays(2));
        lastBookingQuery.setParameter("endDate", LocalDateTime.now().minusDays(1));
        lastBookingQuery.setParameter("itemId", 1L);
        lastBookingQuery.setParameter("status", Statuses.APPROVED);
        lastBookingQuery.setParameter("bookerId", 1L);
        lastBookingQuery.executeUpdate();
    }

    private void createNextBookingInDb() {
        Query nextBookingQuery = em.createNativeQuery("INSERT INTO Bookings (id, start_date, end_date, item_id, status, booker_id) " +
                "VALUES (:id , :startDate , :endDate , :itemId , :status , :bookerId);");
        nextBookingQuery.setParameter("id", "2");
        nextBookingQuery.setParameter("startDate", LocalDateTime.now().plusDays(1));
        nextBookingQuery.setParameter("endDate", LocalDateTime.now().plusDays(2));
        nextBookingQuery.setParameter("itemId", 1L);
        nextBookingQuery.setParameter("status", Statuses.APPROVED);
        nextBookingQuery.setParameter("bookerId", 1L);
        nextBookingQuery.executeUpdate();
    }

    private void createCommentInDb() {
        Query commentQuery = em.createNativeQuery("INSERT INTO Comments (id, text, item_id, author_id, created) " +
                "VALUES (:id , :text , :item_id , :author_id , :created);");
        commentQuery.setParameter("id", "1");
        commentQuery.setParameter("text", "text");
        commentQuery.setParameter("item_id", 1L);
        commentQuery.setParameter("author_id", 1L);
        commentQuery.setParameter("created", LocalDateTime.now().plusDays(10));
        commentQuery.executeUpdate();
    }

    @Test
    void createItemTest() {
        createUserInDb();

        NewItemRequest newRequest = new NewItemRequest("name", "description", Boolean.TRUE, 1L, 1L);

        ItemDto findItem = itemService.create(1L, newRequest);

        assertThat(findItem.getId(), CoreMatchers.notNullValue());
        assertThat(findItem.getName(), Matchers.equalTo(newRequest.getName()));
        assertThat(findItem.getDescription(), Matchers.equalTo(newRequest.getDescription()));
        assertThat(findItem.getAvailable(), Matchers.equalTo(newRequest.getAvailable()));
        assertThat(findItem.getOwnerId(), CoreMatchers.notNullValue());
        assertThat(findItem.getRequestId(), CoreMatchers.notNullValue());
    }

    @Test
    void getItemTest() {
        createUserInDb();
        createItemInDb();
        createLastBookingInDb(); // endDate = now - 1 день
        createNextBookingInDb(); // startDate = now + 1 день
        createCommentInDb();

        AdvancedItemDto loadItem = itemService.findItem(1L, 1L);

        // Получаем ожидаемые даты из тестовых данных
        LocalDateTime expectedLastBooking = LocalDateTime.now().minusDays(1); // endDate последнего бронирования
        LocalDateTime expectedNextBooking = LocalDateTime.now().plusDays(1); // startDate следующего бронирования

        assertThat(loadItem.getId(), notNullValue());
        assertThat(loadItem.getName(), equalTo("name"));
        assertThat(loadItem.getDescription(), equalTo("description"));
        assertThat(loadItem.getAvailable(), is(true));

        // Проверяем lastBooking (должен быть endDate последнего бронирования)
        assertThat(loadItem.getLastBooking(), notNullValue());
        assertThat(loadItem.getLastBooking().getDayOfYear(), equalTo(expectedLastBooking.getDayOfYear()));

        // Проверяем nextBooking (должен быть startDate следующего бронирования)
        assertThat(loadItem.getNextBooking(), notNullValue());
        assertThat(loadItem.getNextBooking().getDayOfYear(), equalTo(expectedNextBooking.getDayOfYear()));

        assertThat(loadItem.getComments(), notNullValue());
        assertThat(loadItem.getOwnerId(), notNullValue());
        assertThat(loadItem.getRequestId(), notNullValue());
    }

    @Test
    void getItemForAnotherUserTest() {
        createUserInDb();
        createItemInDb();
        createLastBookingInDb();
        createNextBookingInDb();
        createCommentInDb();

        AdvancedItemDto loadItem = itemService.findItem(999L, 1L);

        assertThat(loadItem.getId(), CoreMatchers.notNullValue());
        assertThat(loadItem.getName(), Matchers.equalTo("name"));
        assertThat(loadItem.getDescription(), Matchers.equalTo("description"));
        assertThat(String.valueOf(loadItem.getAvailable()), true);
        assertThat(loadItem.getLastBooking(), CoreMatchers.nullValue());
        assertThat(loadItem.getNextBooking(), CoreMatchers.nullValue());
        assertThat(loadItem.getComments(), CoreMatchers.notNullValue());
        assertThat(loadItem.getOwnerId(), CoreMatchers.notNullValue());
        assertThat(loadItem.getRequestId(), CoreMatchers.notNullValue());
    }

    @Test
    void testFindAllWithAdvancedData() {
        createUserInDb();
        createItemInDb();
        createLastBookingInDb();
        createNextBookingInDb();
        createCommentInDb();

        Collection<AdvancedItemDto> loadRequests = itemService.findAll(1L);
        List<AdvancedItemDto> items = loadRequests.stream().toList();

        assertThat(items, hasSize(1));
        assertThat(items, hasItem(allOf(
                hasProperty("id", notNullValue()),
                hasProperty("name", equalTo(items.getFirst().getName())),
                hasProperty("description", equalTo(items.getFirst().getDescription())),
                hasProperty("available", equalTo(items.getFirst().getAvailable())),
                hasProperty("lastBooking", notNullValue()),
                hasProperty("lastBooking", CoreMatchers.instanceOf(LocalDateTime.class)),
                hasProperty("nextBooking", notNullValue()),
                hasProperty("nextBooking", CoreMatchers.instanceOf(LocalDateTime.class)),
                hasProperty("comments", notNullValue()),
                hasProperty("ownerId", equalTo(items.getFirst().getOwnerId())),
                hasProperty("requestId", equalTo(items.getFirst().getRequestId()))
        )));
    }

    @Test
    void testFindAll() {
        createUserInDb();

        List<NewItemRequest> items = List.of(
                makeNewItemRequest("name1", "description1", Boolean.TRUE, 1L, 1L),
                makeNewItemRequest("name2", "description2", Boolean.TRUE, 1L, 2L),
                makeNewItemRequest("name3", "description3", Boolean.TRUE, 1L, 3L)
        );

        for (NewItemRequest itemRequest : items) {
            itemService.create(1L, itemRequest);
        }

        Collection<AdvancedItemDto> loadRequests = itemService.findAll(1L);

        assertThat(loadRequests, hasSize(items.size()));
        for (NewItemRequest item : items) {
            assertThat(loadRequests, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(item.getName())),
                    hasProperty("description", equalTo(item.getDescription())),
                    hasProperty("available", equalTo(item.getAvailable())),
                    hasProperty("lastBooking", nullValue()),
                    hasProperty("nextBooking", nullValue()),
                    hasProperty("comments", CoreMatchers.equalTo(new ArrayList<>())),
                    hasProperty("ownerId", equalTo(item.getOwnerId())),
                    hasProperty("requestId", equalTo(item.getRequestId()))
            )));
        }
    }

    @Test
    void testFindItemsForTenant() {
        createUserInDb();

        List<NewItemRequest> items = List.of(
                makeNewItemRequest("name1", "description1", Boolean.TRUE, 1L, 1L),
                makeNewItemRequest("name2", "description2", Boolean.TRUE, 1L, 2L),
                makeNewItemRequest("name3", "description3", Boolean.TRUE, 1L, 3L)
        );

        for (NewItemRequest itemRequest : items) {
            itemService.create(1L, itemRequest);
        }

        Collection<ItemDto> loadRequests = itemService.findItemsForTenant(1L, "cript");

        assertThat(loadRequests, hasSize(items.size()));
        for (NewItemRequest item : items) {
            assertThat(loadRequests, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(item.getName())),
                    hasProperty("description", equalTo(item.getDescription())),
                    hasProperty("available", equalTo(item.getAvailable())),
                    hasProperty("ownerId", equalTo(item.getOwnerId())),
                    hasProperty("requestId", equalTo(item.getRequestId()))
            )));
        }
    }

    @Test
    void updateItemTest() {
        createUserInDb();
        createItemInDb();

        UpdateItemRequest updItemRequest = new UpdateItemRequest(1L, "name1", "description1", Boolean.FALSE, 1L, 2L);
        ItemDto findItemRequest = itemService.update(1L, updItemRequest, 1L);

        MatcherAssert.assertThat(findItemRequest.getId(), CoreMatchers.equalTo(updItemRequest.getId()));
        MatcherAssert.assertThat(findItemRequest.getName(), Matchers.equalTo(updItemRequest.getName()));
        MatcherAssert.assertThat(findItemRequest.getDescription(), Matchers.equalTo(updItemRequest.getDescription()));
        MatcherAssert.assertThat(findItemRequest.getAvailable(), Matchers.equalTo(updItemRequest.getAvailable()));
        MatcherAssert.assertThat(findItemRequest.getOwnerId(), Matchers.equalTo(updItemRequest.getOwnerId()));
    }

    @Test
    void deleteItemTest() {
        createUserInDb();
        createItemInDb();

        itemService.delete(1L, 1L);

        TypedQuery<Item> selectQuery = em.createQuery("Select i from Item i where i.description like :description", Item.class);
        List<Item> users = selectQuery.setParameter("description", "description1").getResultList();

        MatcherAssert.assertThat(users, CoreMatchers.equalTo(new ArrayList<>()));
    }

    @Test
    void addCommentTest() {
        createUserInDb();
        createItemInDb();
        createLastBookingInDb();
        createNextBookingInDb();

        NewCommentRequest newComment = new NewCommentRequest("comment", 1L, 1L);
        CommentDto findComment = itemService.addComment(1L, 1L, newComment);

        MatcherAssert.assertThat(findComment.getId(), CoreMatchers.notNullValue());
        MatcherAssert.assertThat(findComment.getText(), Matchers.equalTo(newComment.getText()));
        MatcherAssert.assertThat(findComment.getItemId(), Matchers.equalTo(newComment.getItemId()));
        MatcherAssert.assertThat(findComment.getAuthorName(), CoreMatchers.notNullValue());
        MatcherAssert.assertThat(findComment.getCreated(), CoreMatchers.notNullValue());
    }

    private NewItemRequest makeNewItemRequest(String name, String description, Boolean available, Long ownerId, Long requestId) {
        NewItemRequest dto = new NewItemRequest();
        dto.setName(name);
        dto.setDescription(description);
        dto.setAvailable(available);
        dto.setOwnerId(ownerId);
        dto.setRequestId(requestId);

        return dto;
    }
}
