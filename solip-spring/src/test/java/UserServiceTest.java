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

    @Test
    public void bean() {
        Assert.assertThat(userService, CoreMatchers.is(CoreMatchers.notNullValue()));
    }

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, 49, 0)
                , new User("joytouch", "강명성", "p2", Level.BASIC, 50, 0)
                , new User("erwins", "신승한", "p3", Level.SILVER, 60, 29)
                , new User("madnite1", "이상호", "p4", Level.SILVER, 60, 30)
                , new User("green", "오민규", "p5", Level.GOLD, 100, 100)
        );
    }

    @Test
    public void upgradeLevels() {

        userDao.deleteAll();

        for(User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);

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

    private void checkLevel(User user, Level expectedLevel) {
        User userUpdate = userDao.get(user.getId());
        Assert.assertThat(userUpdate.getLevel(), CoreMatchers.is(expectedLevel));
    }

}
