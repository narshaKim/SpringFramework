package dao;

import domain.User;

import java.util.List;

public interface UserDao {

    public void add(final User user);

    public User get(final String id);

    public List<User> getAll();

    public void deleteAll();

    public long getCount();

    public void update(User user);
}
