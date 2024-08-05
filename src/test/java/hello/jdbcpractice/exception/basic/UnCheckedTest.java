package hello.jdbcpractice.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class UnCheckedTest {

    @Test
    void unChecked_catch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void unChecked_throws() {
        Service service = new Service();
        assertThatThrownBy(service::callThrows)
                .isInstanceOf(CheckedTest.MyCheckedException.class);

    }

    /**
     * RuntimeException을 상속받은 예외는 언체킁 예외가 된다
     */
    static class MyUncheckedException extends RuntimeException {
        public MyUncheckedException(String message) {
            super(message);
        }
    }

    /**
     * UnChecked 예외는
     * 예외를 잡거나 던지지 않아도 된다.
     * 예외를 잡지 않으면 자동으로 밖으로 던진다
     */
    static class Service {
        Repository repository = new Repository();

        public void callCatch() {
            try {
                repository.call();
            } catch (MyUncheckedException e) {
                log.info("예외 처리 message={}", e.getMessage());
            }
        }

        /**
         * 예외를 잡지 않아도 된다. 자연스럽게 상위로 올라간다.
         * 체크 예외와 다르게 throws 선언을 하지 않아도 된다.
         */
        public void callThrows() {
            repository.call();
        }
    }

    static class Repository {
        public void call() {
            throw new MyUncheckedException("MyUncheckedException");
        }
    }
}
