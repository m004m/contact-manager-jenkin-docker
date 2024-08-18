package com.smcontactm.usercontrolltest;



import org.junit.jupiter.api.Test;
import org.junit.Assert;

import java.security.Principal;

public class UserControllerTest extends UserControllBaseTest{

    Principal principal;

    @Test
    public void checkConcatListEmptyTest(){

        Assert.assertTrue(contactList(principal));

    }
}
