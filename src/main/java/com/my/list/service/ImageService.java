package com.my.list.service;

import com.my.list.domain.Image;
import com.my.list.domain.User;
import com.my.list.service.interfaces.SingleCrudService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService implements SingleCrudService<Image> {
    
    public Image postResource(User user, Image newResource) {
        return null;
    }

    public Image getResource(User user, Long resourceId) {
        return null;
    }

    public List<Image> getAllResources(User user) {
        return new ArrayList<>();
    }

    public Image putResource(User user, Image updatedResource) {
        return null;
    }

    public void deleteResource(User user, Long resourceId) {

    }
}
