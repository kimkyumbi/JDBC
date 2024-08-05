package hello.jdbcpractice.exception.basic;

import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

@Slf4j
public class CheckedAppTest {

    @Test
    void checked() {
        Controller controller = new Controller();
        assertThatThrownBy(controller::request).isInstanceOf(ConnectException.class);
    }

    static class Controller {
        Service service = new Service();

        public void request() throws SQLException, ConnectException {
            service.lozic();
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetWorkClient netWorkClient = new NetWorkClient();

        public void lozic() throws ConnectException, SQLException {
            netWorkClient.call();
            repository.call();
        }
    }

    static class NetWorkClient {
        public void call() throws ConnectException {
            throw new ConnectException("연결 실패");
        }
    }

    static class Repository {
        public void call() throws SQLException {
            throw new SQLException("ex");
        }
    }
}
