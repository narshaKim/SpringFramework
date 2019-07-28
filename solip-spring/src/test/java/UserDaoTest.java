import dao.UserDao;
import domain.Level;
import domain.User;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/spring-config.xml")
public class UserDaoTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private UserDao userDao;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {

        System.out.println(this);
        System.out.println(this.context);

        user1 = new User("aaa", "AAA", "aPassword", Level.BASIC, 1, 0);
        user2 = new User("bbb", "BBB", "bPassword", Level.SILVER, 55, 10);
        user3 = new User("ccc", "CCC", "cPassword", Level.GOLD, 100, 40);

    }

    @Test
    public void addAndGet() {

        userDao.deleteAll();
        Assert.assertThat(userDao.getCount(), CoreMatchers.is(0L));

        userDao.add(user1);
        Assert.assertThat(userDao.getCount(), CoreMatchers.is(1L));

        User user2 = userDao.get(user1.getId());

        Assert.assertThat(user2.getName(), CoreMatchers.is(user1.getName()));
        Assert.assertThat(user2.getPassword(), CoreMatchers.is(user1.getPassword()));
        Assert.assertThat(user2.getLevel(), CoreMatchers.is(user1.getLevel()));
        Assert.assertThat(user2.getLogin(), CoreMatchers.is(user1.getLogin()));
        Assert.assertThat(user2.getRecommend(), CoreMatchers.is(user1.getRecommend()));

    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getFailure() {

        userDao.deleteAll();
        Assert.assertThat(userDao.getCount(), CoreMatchers.is(0L));

        User getUser1 = userDao.get(user1.getId());
        Assert.assertThat(getUser1.getName(), CoreMatchers.is(user1.getName()));
        Assert.assertThat(getUser1.getPassword(), CoreMatchers.is(user1.getPassword()));
        Assert.assertThat(getUser1.getLevel(), CoreMatchers.is(user1.getLevel()));
        Assert.assertThat(getUser1.getLogin(), CoreMatchers.is(user1.getLogin()));
        Assert.assertThat(getUser1.getRecommend(), CoreMatchers.is(user1.getRecommend()));

        User getUser2 = userDao.get(user2.getId());
        Assert.assertThat(getUser2.getName(), CoreMatchers.is(user2.getName()));
        Assert.assertThat(getUser2.getPassword(), CoreMatchers.is(user2.getPassword()));
        Assert.assertThat(getUser2.getLevel(), CoreMatchers.is(user2.getLevel()));
        Assert.assertThat(getUser2.getLogin(), CoreMatchers.is(user2.getLogin()));
        Assert.assertThat(getUser2.getRecommend(), CoreMatchers.is(user2.getRecommend()));

    }

    @Test
    public void getAll() {

        userDao.deleteAll();

        List<User> users0 = (List<User>) userDao.getAll();
        Assert.assertThat(users0.size(), CoreMatchers.is(0));

        userDao.add(user1);
        List<User> users1 = (List<User>) userDao.getAll();
        Assert.assertThat(users1.size(), CoreMatchers.is(1));
        checkSameUser(user1, users1.get(0));

        userDao.add(user2);
        List<User> users2 = (List<User>) userDao.getAll();
        Assert.assertThat(users2.size(), CoreMatchers.is(2));
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        userDao.add(user3);
        List<User> users3 = (List<User>) userDao.getAll();
        Assert.assertThat(users3.size(), CoreMatchers.is(3));
        checkSameUser(user1, users3.get(0));
        checkSameUser(user2, users3.get(1));
        checkSameUser(user3, users3.get(2));

    }

    @Test(expected = DataAccessException.class)
    public void duplicateKey() {
        userDao.deleteAll();

        userDao.add(user1);
        userDao.add(user1);
    }

    @Test
    public void update() {
        userDao.deleteAll();

        userDao.add(user1);
        userDao.add(user2);

        user1.setName("수정된이름");
        user1.setPassword("modified");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        userDao.update(user1);

        User user1Update = userDao.get(user1.getId());
        checkSameUser(user1, user1Update);

        User user2same = userDao.get(user2.getId());
        checkSameUser(user2, user2same);

    }

    private void checkSameUser(User user1, User user2) {
        Assert.assertThat(user1.getId(), CoreMatchers.is(user2.getId()));
        Assert.assertThat(user1.getName(), CoreMatchers.is(user2.getName()));
        Assert.assertThat(user1.getPassword(), CoreMatchers.is(user2.getPassword()));
        Assert.assertThat(user1.getLevel(), CoreMatchers.is(user2.getLevel()));
        Assert.assertThat(user1.getLogin(), CoreMatchers.is(user2.getLogin()));
        Assert.assertThat(user1.getRecommend(), CoreMatchers.is(user2.getRecommend()));
    }

}
