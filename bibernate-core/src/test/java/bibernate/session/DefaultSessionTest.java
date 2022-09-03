package bibernate.session;

import bibernate.session.mocks.User;
import com.bobocode.petros.bibernate.session.DefaultSession;
import com.bobocode.petros.bibernate.session.Session;
import com.bobocode.petros.bibernate.session.jdbc.JdbcQueryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultSessionTest {

    @Mock
    private DataSource dataSource;
    @Mock
    private JdbcQueryManager jdbcQueryManager;
    @Mock
    private User user;
    private Session session;

    @BeforeEach
    private void init() {
        MockitoAnnotations.openMocks(this);
        session = new DefaultSession(dataSource, jdbcQueryManager);
    }

    @Test
    void whenPersistEntityThenCallMethodPersistIntoJdbcQueryManager() {
        session.persist(user);

        verify(jdbcQueryManager, times(1)).persist(user);
        verifyNoMoreInteractions(jdbcQueryManager);
    }

    @Test
    void whenUpdateEntityThenCallMethodUpdateIntoJdbcQueryManager() {
        session.update(user);

        verify(jdbcQueryManager, times(1)).update(user);
        verifyNoMoreInteractions(jdbcQueryManager);
    }

    @Test
    void whenFindEntityByIdThenCallMethodFindIntoJdbcQueryManager() {
        session.findById(User.class, 1L);

        verify(jdbcQueryManager, times(1)).find(any(), any());
        verifyNoMoreInteractions(jdbcQueryManager);
    }

    @Test
    void whenDeleteEntityThenCallMethodDeleteIntoJdbcQueryManager(){
        Long id = 1L;
        session.delete(user);

        verify(jdbcQueryManager, times(1)).delete(any(),any());
        verifyNoMoreInteractions(jdbcQueryManager);

        session.deleteById(id);

        verify(jdbcQueryManager, times(1)).deleteById(id);
        verifyNoMoreInteractions(jdbcQueryManager);
    }
}
