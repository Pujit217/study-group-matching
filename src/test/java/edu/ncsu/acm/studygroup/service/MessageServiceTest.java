package edu.ncsu.acm.studygroup.service;

import edu.ncsu.acm.studygroup.dto.MessageDto;
import edu.ncsu.acm.studygroup.entity.Message;
import edu.ncsu.acm.studygroup.entity.StudyGroup;
import edu.ncsu.acm.studygroup.entity.User;
import edu.ncsu.acm.studygroup.repository.MessageRepository;
import edu.ncsu.acm.studygroup.repository.StudyGroupRepository;
import edu.ncsu.acm.studygroup.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private StudyGroupRepository studyGroupRepository;

    @Mock
    private UserRepository userRepository;

    private MessageService messageService;

    @BeforeEach
    void setUp() {
        messageService = new MessageService(
                messageRepository,
                studyGroupRepository,
                userRepository
        );
    }

    @Test
    void getAllMessagesReturnsRepositoryMessages() {
        Message message1 = new Message();
        Message message2 = new Message();

        when(messageRepository.findAll())
                .thenReturn(List.of(message1, message2));

        List<Message> result = messageService.getAllMessages();

        assertEquals(2, result.size());
        assertSame(message1, result.get(0));
        assertSame(message2, result.get(1));

        verify(messageRepository).findAll();
    }

    @Test
    void getMessageByIdReturnsMessageWhenFound() {
        Message message = new Message();

        when(messageRepository.findById(1L))
                .thenReturn(Optional.of(message));

        Optional<Message> result =
                messageService.getMessageById(1L);

        assertTrue(result.isPresent());
        assertSame(message, result.get());

        verify(messageRepository).findById(1L);
    }

    @Test
    void getMessageByIdReturnsEmptyWhenNotFound() {
        when(messageRepository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<Message> result =
                messageService.getMessageById(99L);

        assertTrue(result.isEmpty());

        verify(messageRepository).findById(99L);
    }

    @Test
    void getMessagesForGroupReturnsMessagesInRepositoryOrder() {
        StudyGroup studyGroup = new StudyGroup();

        Message message1 = new Message();
        Message message2 = new Message();

        when(messageRepository
                .findByStudyGroupOrderBySentAtAsc(studyGroup))
                .thenReturn(List.of(message1, message2));

        List<Message> result =
                messageService.getMessagesForGroup(studyGroup);

        assertEquals(2, result.size());
        assertSame(message1, result.get(0));
        assertSame(message2, result.get(1));

        verify(messageRepository)
                .findByStudyGroupOrderBySentAtAsc(studyGroup);
    }

    @Test
    void getMessagesBySenderReturnsSenderMessages() {
        User sender = new User();

        Message message1 = new Message();
        Message message2 = new Message();

        when(messageRepository.findBySender(sender))
                .thenReturn(List.of(message1, message2));

        List<Message> result =
                messageService.getMessagesBySender(sender);

        assertEquals(2, result.size());
        assertSame(message1, result.get(0));
        assertSame(message2, result.get(1));

        verify(messageRepository).findBySender(sender);
    }

    @Test
    void sendMessageCreatesAndSavesMessage() {
        StudyGroup studyGroup = new StudyGroup();
        User sender = new User();

        MessageDto dto = new MessageDto();
        dto.setStudyGroupId(1L);
        dto.setSenderId(2L);
        dto.setContent("  Hello everyone!  ");

        when(studyGroupRepository.findById(1L))
                .thenReturn(Optional.of(studyGroup));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(sender));

        when(messageRepository.save(any(Message.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Message result = messageService.sendMessage(dto);

        assertNotNull(result);
        assertSame(studyGroup, result.getStudyGroup());
        assertSame(sender, result.getSender());

        // Service should trim whitespace.
        assertEquals("Hello everyone!", result.getContent());

        // send() should assign the sent time.
        assertNotNull(result.getSentAt());

        ArgumentCaptor<Message> captor =
                ArgumentCaptor.forClass(Message.class);

        verify(messageRepository).save(captor.capture());

        Message savedMessage = captor.getValue();

        assertSame(studyGroup, savedMessage.getStudyGroup());
        assertSame(sender, savedMessage.getSender());
        assertEquals("Hello everyone!", savedMessage.getContent());
    }

    @Test
    void sendMessageThrowsExceptionWhenStudyGroupDoesNotExist() {
        MessageDto dto = new MessageDto();
        dto.setStudyGroupId(99L);
        dto.setSenderId(1L);
        dto.setContent("Hello");

        when(studyGroupRepository.findById(99L))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> messageService.sendMessage(dto)
                );

        assertEquals(
                "Study group not found",
                exception.getMessage()
        );

        verify(messageRepository, never())
                .save(any(Message.class));
    }

    @Test
    void sendMessageThrowsExceptionWhenUserDoesNotExist() {
        StudyGroup studyGroup = new StudyGroup();

        MessageDto dto = new MessageDto();
        dto.setStudyGroupId(1L);
        dto.setSenderId(99L);
        dto.setContent("Hello");

        when(studyGroupRepository.findById(1L))
                .thenReturn(Optional.of(studyGroup));

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> messageService.sendMessage(dto)
                );

        assertEquals(
                "User not found",
                exception.getMessage()
        );

        verify(messageRepository, never())
                .save(any(Message.class));
    }

    @Test
    void sendMessageThrowsExceptionWhenContentIsNull() {
        StudyGroup studyGroup = new StudyGroup();
        User sender = new User();

        MessageDto dto = new MessageDto();
        dto.setStudyGroupId(1L);
        dto.setSenderId(2L);
        dto.setContent(null);

        when(studyGroupRepository.findById(1L))
                .thenReturn(Optional.of(studyGroup));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(sender));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> messageService.sendMessage(dto)
                );

        assertEquals(
                "Message content cannot be empty",
                exception.getMessage()
        );

        verify(messageRepository, never())
                .save(any(Message.class));
    }

    @Test
    void sendMessageThrowsExceptionWhenContentIsBlank() {
        StudyGroup studyGroup = new StudyGroup();
        User sender = new User();

        MessageDto dto = new MessageDto();
        dto.setStudyGroupId(1L);
        dto.setSenderId(2L);
        dto.setContent("     ");

        when(studyGroupRepository.findById(1L))
                .thenReturn(Optional.of(studyGroup));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(sender));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> messageService.sendMessage(dto)
                );

        assertEquals(
                "Message content cannot be empty",
                exception.getMessage()
        );

        verify(messageRepository, never())
                .save(any(Message.class));
    }

    @Test
    void editMessageUpdatesContentAndSavesMessage() {
        Message message = new Message();
        message.setContent("Old message");

        when(messageRepository.save(message))
                .thenReturn(message);

        Message result =
                messageService.editMessage(
                        message,
                        "Updated message"
                );

        assertEquals(
                "Updated message",
                result.getContent()
        );

        assertNotNull(result.getEditedAt());

        verify(messageRepository).save(message);
    }

    @Test
    void deleteMessageDeletesMessageById() {
        messageService.deleteMessage(5L);

        verify(messageRepository).deleteById(5L);
    }
}