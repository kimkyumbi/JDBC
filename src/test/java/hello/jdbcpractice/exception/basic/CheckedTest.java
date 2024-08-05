package hello.jdbcpractice.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class CheckedTest {

    @Test
    void checked_catch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void checked_throws() {
        Service service = new Service();
        assertThatThrownBy(service::callThrows)
                .isInstanceOf(MyCheckedException.class);
    }

    /**
     * Exception을 상속받은 예외는 체크 예외가 된다.
     */
    static class MyCheckedException extends Exception {
        public MyCheckedException(String message) {
            super(message);
        }
    }

    /**
     * check 예외는
     * 예외를 잡아서 처리하거나 던지거나 둘중에 하나를 필수로 선택해야 한다.
     */
    static class Service {
        Repository repo = new Repository();

        /**
         * 예외를 잡아서 처리하는 코드
         */
        public void callCatch() {
            try {
                repo.call();
            } catch (MyCheckedException e) {
                log.info("예외 처리: message={}", e.getMessage());
            }
        }

        /**
         * 체크 예외를 밖으로 던지는 코드
         * 체크 예외는 예외를 잡지 않고 던지려면 throws 예외를 메서드에 반드시 선언해야 한다.
         */
        public void callThrows() throws MyCheckedException {
            repo.call();
        }
    }

    static class Repository {
        public void call() throws MyCheckedException {
            throw new MyCheckedException("Error");
        }
    }

}
