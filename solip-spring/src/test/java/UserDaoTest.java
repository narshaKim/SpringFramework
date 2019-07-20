import dao.UserDao;
import domain.User;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.sql.SQLException;

public class UserDaoTest {

    @Test
    public void addAndGet() throws SQLException {

        ApplicationContext context = new GenericXmlApplicationContext("spring-config.xml");

        UserDao userDao = context.getBean("userDao", UserDao.class);

        userDao.deleteAll();
        Assert.assertThat(userDao.getCount(), CoreMatchers.is(0));

        User user = new User();
        user.setId("user");
        user.setName("솔잎");
        user.setPassword("thfdlv");

        userDao.add(user);
        Assert.assertThat(userDao.getCount(), CoreMatchers.is(1));

        User user2 = userDao.get(user.getId());

        Assert.assertThat(user2.getName(), CoreMatchers.is(user.getName()));
        Assert.assertThat(user2.getPassword(), CoreMatchers.is(user.getPassword()));

    }

}
