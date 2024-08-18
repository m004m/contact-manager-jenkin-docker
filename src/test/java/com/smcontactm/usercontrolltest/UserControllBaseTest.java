package com.smcontactm.usercontrolltest;

import com.smcontactm.repository.ContactRepository;
import com.smcontactm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.security.Principal;

@SpringBootTest
public class UserControllBaseTest {

    @Autowired
    private ContactRepository contactRepository;

    int currentPageNo = 1;
    int perPage = 2;

    @Autowired
    private UserRepository userRepository;

    protected boolean contactList(Principal principal) {

        boolean isContactList = false;

       // User user = userRepository.getUserByUserName(principal.getName());
        Pageable pageable = PageRequest.of(currentPageNo, perPage);
        int totlContact = contactRepository.getCountContactsByUser(50);
        //Page<Contact> listContacts = this.contactRepository.findContactsByUserPagination(user.getId(), pageable);

        if (totlContact > 0) {
            isContactList = true;
        }
        return isContactList;

    }
}
