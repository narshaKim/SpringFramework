import dao.UserDao;
import domain.Level;
import domain.User;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import service.TestUserService;
import service.UserService;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/spring-config.xml")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    List<User> users;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    public void bean() {
        Assert.assertThat(userService, CoreMatchers.is(CoreMatchers.notNullValue()));
    }

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, UserService.MIN_LOGCOUNT_FOR_SILVER-1, 0, "bumjin@spring.com")
                , new User("joytouch", "강명성", "p2", Level.BASIC, UserService.MIN_LOGCOUNT_FOR_SILVER, 0, "joytouch@spring.com")
                , new User("erwins", "신승한", "p3", Level.SILVER, 60, UserService.MIN_RECCOMEND_FOR_GOLD-1, "erwins@spring.com")
                , new User("madnite1", "이상호", "p4", Level.SILVER, 60, UserService.MIN_RECCOMEND_FOR_GOLD, "madnite1@spring.com")
                , new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "green@spring.com")
        );
    }

    @Test
    public void upgradeLevels() {

        userDao.deleteAll();

        for(User user : users) {
            userDao.add(user);
        }

        try {
            userService.upgradeLevels();
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkLevel(users.get(0), false);
        checkLevel(users.get(1), true);
        checkLevel(users.get(2), false);
        checkLevel(users.get(3), true);
        checkLevel(users.get(4), false);

    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userwithoutLevel = users.get(0);
        userWithLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userwithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userwithoutLevel.getId());

        Assert.assertThat(userWithLevelRead.getLevel(), CoreMatchers.is(userWithLevel.getLevel()));
        Assert.assertThat(userWithoutLevelRead.getLevel(), CoreMatchers.is(Level.BASIC));

    }

    @Test
    public void upgradeAllNothing() {
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setTransactionManager(transactionManager);

        userDao.deleteAll();

        for(User user : users) {
            userDao.add(user);
        }

        try {
            testUserService.upgradeLevels();
            Assert.fail("TestUserServiceException expected");
        } catch (Exception e) {
        }

        checkLevel(users.get(1), false);


    }

    private void checkLevel(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if(upgraded)
            Assert.assertThat(userUpdate.getLevel(), CoreMatchers.is(user.getLevel().nextLevel()));
        else
            Assert.assertThat(userUpdate.getLevel(), CoreMatchers.is(user.getLevel()));
    }

}
