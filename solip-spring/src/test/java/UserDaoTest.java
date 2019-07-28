import dao.UserDao;
import domain.User;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
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

        user1 = new User("aaa", "AAA", "aPassword");
        user2 = new User("bbb", "BBB", "bPassword");
        user3 = new User("ccc", "CCC", "cPassword");

    }

    @Test
    public void addAndGet() throws SQLException {

        userDao.deleteAll();
        Assert.assertThat(userDao.getCount(), CoreMatchers.is(0L));

        userDao.add(user1);
        Assert.assertThat(userDao.getCount(), CoreMatchers.is(1L));

        User user2 = userDao.get(user1.getId());

        Assert.assertThat(user2.getName(), CoreMatchers.is(user1.getName()));
        Assert.assertThat(user2.getPassword(), CoreMatchers.is(user1.getPassword()));

    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getFailure() throws SQLException {

        userDao.deleteAll();
        Assert.assertThat(userDao.getCount(), CoreMatchers.is(0L));

        User getUser1 = userDao.get(user1.getId());
        Assert.assertThat(getUser1.getName(), CoreMatchers.is(user1.getName()));
        Assert.assertThat(getUser1.getPassword(), CoreMatchers.is(user1.getPassword()));

        User getUser2 = userDao.get(user2.getId());
        Assert.assertThat(getUser2.getName(), CoreMatchers.is(user2.getName()));
        Assert.assertThat(getUser2.getPassword(), CoreMatchers.is(user2.getPassword()));

    }

    @Test
    public void getAll() throws SQLException {

        userDao.deleteAll();

        List<User> users0 = (List<User>) userDao.getAll();
        Assert.assertThat(users0.size(), CoreMatchers.is(0));

        userDao.add(user1);
        List<User> users1 = (List<User>) userDao.getAll();
        Assert.assertThat(users1.size(), CoreMatchers.is(1));
        checkSmaeUser(user1, users1.get(0));

        userDao.add(user2);
        List<User> users2 = (List<User>) userDao.getAll();
        Assert.assertThat(users2.size(), CoreMatchers.is(2));
        checkSmaeUser(user1, users2.get(0));
        checkSmaeUser(user2, users2.get(1));

        userDao.add(user3);
        List<User> users3 = (List<User>) userDao.getAll();
        Assert.assertThat(users3.size(), CoreMatchers.is(3));
        checkSmaeUser(user1, users3.get(0));
        checkSmaeUser(user2, users3.get(1));
        checkSmaeUser(user3, users3.get(2));

    }

    private void checkSmaeUser(User user1, User user2) {
        Assert.assertThat(user1.getId(), CoreMatchers.is(user2.getId()));
        Assert.assertThat(user1.getName(), CoreMatchers.is(user2.getName()));
        Assert.assertThat(user1.getPassword(), CoreMatchers.is(user2.getPassword()));
    }

}
