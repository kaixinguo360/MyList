package com.my.list.service;

import com.my.list.data.MyList;
import com.my.list.data.MyListRepository;
import com.my.list.data.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Service
public class MyListService {

    private final Logger logger = LoggerFactory.getLogger(MyListService.class);
    private final MyListRepository myListRepository;

    @Autowired
    public MyListService(MyListRepository myListRepository) {
        this.myListRepository = myListRepository;
    }

    //Get
    @NotNull
    public MyList getList(@NotNull User user, int listId) throws DataException {
        MyList list = myListRepository.findById(listId).orElse(null);
        if (list != null && list.getUserId() == user.getId()) {
            return list;
        } else {
            logger.info("removeList: List(" + listId + ") Not Exist");
            throw new DataException("removeList: List(" + listId + ") Not Exist", ErrorType.NOT_FOUND);
        }
    }

    //GetAll
    @NotNull
    public Iterable<MyList> getAllLists(@NotNull User user) {
        return myListRepository.findAllByUserId(user.getId());
    }

    //Search
    @NotNull
    public Iterable<MyList> search(@NotNull User user, String title) {
        if(StringUtils.isEmpty(title))
            return new ArrayList<>();
        return myListRepository.findAllByUserIdAndTitleLike(user.getId(), title);
    }

    //Add
    public void addList(@NotNull User user, @NotNull MyList list) throws DataException {
        try {
            list.setUserId(user.getId());
            myListRepository.save(list);
        } catch (Exception e) {
            logger.info("addList: An Error Occurred: " + e.getMessage());
            throw new DataException("An Error Occurred", ErrorType.UNKNOWN_ERROR);
        }
    }

    //Remove
    public void removeList(User user, int listId) throws DataException {
        try {
            getList(user, listId);
            myListRepository.deleteById(listId);
        } catch (DataException e) {
            throw e;
        } catch (Exception e) {
            logger.info("removeList: An Error Occurred: " + e.getMessage());
            throw new DataException("An Error Occurred", ErrorType.UNKNOWN_ERROR);
        }
    }

    //Update
    @Transactional
    public void updateList(@NotNull User user, int listId, @NotNull MyList newList) throws DataException {
        MyList list = getList(user, listId);
        list.setTitle(newList.getTitle());
        list.setInfo(newList.getInfo());
    }
}
