package bibernate.session;

import bibernate.session.mocks.User;
import com.bobocode.petros.bibernate.session.DefaultSession;
import com.bobocode.petros.bibernate.session.Session;
import com.bobocode.petros.bibernate.session.jdbc.JdbcQueryManager;
import com.bobocode.petros.bibernate.session.query.condition.Restriction;
import com.bobocode.petros.bibernate.session.query.condition.Restrictions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class DefaultSessionTest {

    @Mock
    private DataSource dataSource;
    @Mock
    private JdbcQueryManager jdbcQueryManager;
    private Session session;
    private User user;
    private User updatedUser;
    private User persistUser;

    @BeforeEach
    private void init() {
        MockitoAnnotations.openMocks(this);
        setUpUserInstances();
        stubbingMock();
        session = new DefaultSession(dataSource, jdbcQueryManager);
    }

    private void setUpUserInstances() {
        user = new User();
        user.setName("Kirill Kotenok");
        user.setEmail("some@some");

        persistUser = new User();
        persistUser.setId(1L);
        persistUser.setName("Kirill Kotenok");
        persistUser.setEmail("some@some");

        updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Kirill Kotenok");
        updatedUser.setEmail("changed@email");
    }

    private void stubbingMock() {
        jdbcQueryManager = mock(JdbcQueryManager.class);

        lenient().when(jdbcQueryManager.persist(user)).thenReturn(persistUser);
        lenient().when(jdbcQueryManager.update(updatedUser)).thenReturn(updatedUser);
        lenient().when(jdbcQueryManager.find(User.class, List.of(Restrictions.idEq("id", 1L))))
                .thenReturn(persistUser);
    }

    @Test
    void whenPersistEntityThenReturnEntityWithId() {
        var userFromDb = session.persist(user);
        assertAll(
                () -> assertNotNull(user),
                () -> assertNotNull(userFromDb),
                () -> assertNotNull(userFromDb.getId()),
                () -> assertEquals(userFromDb, persistUser),
                () -> assertEquals(persistUser.getName(), user.getName()),
                () -> assertEquals(persistUser.getEmail(), user.getEmail()));
    }

    @Test
    void whenUpdateEntityThenPushChangeToDbAndReturnUpdatedEntity() {
        var updated = session.update(updatedUser);

        assertAll(
                () -> assertNotNull(updated),
                () -> assertNotNull(updatedUser),
                () -> assertEquals(updated, updatedUser),
                () -> assertEquals(updated.getName(), updatedUser.getName()),
                () -> assertEquals(updatedUser.getEmail(), updated.getEmail()),
                () -> assertEquals(updated.getId(), updatedUser.getId()));
    }

    @Test
    void whenFindEntityByIdThenReturnPersistEntity() {
        var findUser = session.findById(User.class, 1L);

        assertAll(
                () -> assertNotNull(findUser),
                () -> assertEquals(findUser, user),
                () -> assertEquals(findUser.getId(), 1L),
                () -> assertEquals(findUser, persistUser)
        );
    }
}
