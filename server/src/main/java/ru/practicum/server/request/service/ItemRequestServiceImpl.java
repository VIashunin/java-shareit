package ru.practicum.server.request.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.server.exceptions.PageableParametersAreInvalidException;
import ru.practicum.server.exceptions.RequestNotFoundException;
import ru.practicum.server.item.mapper.ItemMapper;
import ru.practicum.server.item.repository.ItemRepository;
import ru.practicum.server.request.dto.ItemRequestDto;
import ru.practicum.server.request.mapper.ItemRequestMapper;
import ru.practicum.server.request.repository.ItemRequestRepository;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@NoArgsConstructor
@Transactional
public class ItemRequestServiceImpl implements ItemRequestService {
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserService userService;

    @Override
    public ItemRequestDto addRequest(long userId, ItemRequestDto itemRequestDto) {
        User user = userService.findUserById(userId);
        itemRequestDto.setRequestor(userId);
        if (itemRequestDto.getCreated() == null) {
            itemRequestDto.setCreated(LocalDateTime.now());
        }
        return ItemRequestMapper.mapFromItemRequestToItemRequestDto(
                itemRequestRepository.save(ItemRequestMapper.mapFromItemRequestDtoToItemRequest(itemRequestDto, user)));
    }

    @Override
    public List<ItemRequestDto> getRequest(long userId) {
        userService.findUserById(userId);
        List<ItemRequestDto> itemRequestDtoList = ItemRequestMapper.mapFromItemRequestListToItemRequestDtoList(
                itemRequestRepository.findItemRequestByRequestor_Id(userId));
        for (ItemRequestDto itemRequestDto : itemRequestDtoList) {
            itemRequestDto.setItems(ItemMapper.mapFromItemListToItemDtoList(itemRepository.findItemByRequest_Id(itemRequestDto.getId())));
        }
        return itemRequestDtoList;
    }

    @Override
    public List<ItemRequestDto> getAllRequests(long userId, Integer from, Integer size) {
        userService.findUserById(userId);
        Pageable page = paginate(from, size);
        List<ItemRequestDto> itemRequestDtoList = ItemRequestMapper.mapFromItemRequestListToItemRequestDtoList(
                itemRequestRepository.findItemRequestByIdNotOrderByCreatedDesc(userId, page));
        for (ItemRequestDto itemRequestDto : itemRequestDtoList) {
            itemRequestDto.setItems(ItemMapper.mapFromItemListToItemDtoList(itemRepository.findItemByRequest_Id(itemRequestDto.getId())));
        }
        return itemRequestDtoList;
    }

    @Override
    public ItemRequestDto getRequestById(long userId, long requestId) {
        userService.findUserById(userId);
        ItemRequestDto itemRequestDto = ItemRequestMapper.mapFromItemRequestToItemRequestDto(itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException(requestId)));
        itemRequestDto.setItems(ItemMapper.mapFromItemListToItemDtoList(itemRepository.findItemByRequest_Id(itemRequestDto.getId())));
        return itemRequestDto;
    }

    public Pageable paginate(Integer from, Integer size) {
        return getPageable(from, size);
    }

    public static Pageable getPageable(Integer from, Integer size) {
        Pageable page;
        if (from != null && size != null) {
            if (from < 0 || size < 0) {
                throw new PageableParametersAreInvalidException("From or size pageable parameters are invalid.");
            }
            page = PageRequest.of(from > 0 ? from / size : 0, size);
        } else {
            page = PageRequest.of(0, 4);
        }
        return page;
    }
}
