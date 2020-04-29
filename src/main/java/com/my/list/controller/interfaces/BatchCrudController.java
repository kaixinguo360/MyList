package com.my.list.controller.interfaces;

import com.my.list.domain.User;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BatchCrudController<T> {

    /**
     * POST /{resource}/batch
     */
    List<T> postResources(User user, @RequestBody List<T> newResources);

    /**
     * GET /{resource}/batch/{ids}
     */
    List<T> getResources(User user, List<Long> resourceIds, @RequestParam @Nullable Integer limit, @RequestParam @Nullable Integer offset);

    /**
     * PUT /{resource}/batch
     */
    List<T> putResources(User user, @RequestBody List<T> updatedResources);

    /**
     * DELETE /{resource}/batch
     */
    void deleteResources(User user, @RequestBody List<Long> resourceIds);
}
