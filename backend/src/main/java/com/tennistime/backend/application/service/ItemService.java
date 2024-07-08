package com.tennistime.backend.application.service;

import com.tennistime.backend.domain.model.Item;
import com.tennistime.backend.domain.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }

    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    public Item findItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    public void deleteItemById(Long id) {
        itemRepository.deleteById(id);
    }
}
