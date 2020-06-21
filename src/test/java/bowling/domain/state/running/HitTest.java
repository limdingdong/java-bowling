package bowling.domain.state.running;

import bowling.domain.pin.PinCount;
import bowling.domain.pin.Pins;
import bowling.domain.state.StateExpression;
import bowling.domain.state.finish.Miss;
import bowling.domain.state.finish.Spare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class HitTest {

    @DisplayName("Spare 상태를 가질 수 없으면 예외 반환")
    @ParameterizedTest
    @ValueSource(ints = { -1, 10 })
    public void createFailure(final int count) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Hit.of(Pins.of(count)));
    }

    @DisplayName("두번째 투구에서 나머지 볼링 핀이 모두 넘어지면 Spare 반환")
    @ParameterizedTest
    @MethodSource
    public void returnSpare(final Hit hit, final PinCount pinCount) {
        assertThat(hit.bowl(pinCount))
                .isInstanceOf(Spare.class);
    }

    private static Stream<Arguments> returnSpare() {
        return Stream.of(
                Arguments.of(Hit.of(Pins.of(9)), PinCount.of(PinCount.MAX_COUNT - 9)),
                Arguments.of(Hit.of(Pins.of(2)), PinCount.of(PinCount.MAX_COUNT - 2)),
                Arguments.of(Hit.of(Pins.of(PinCount.MIN_COUNT)), PinCount.of(PinCount.MAX_COUNT))
        );
    }

    @DisplayName("두번째 투구에서 볼링 핀이 1개라도 남아있으면 Miss 반환")
    @ParameterizedTest
    @MethodSource
    public void returnMiss(final Hit hit, final PinCount pinCount) {
        assertThat(hit.bowl(pinCount))
                .isInstanceOf(Miss.class);
    }

    private static Stream<Arguments> returnMiss() {
        return Stream.of(
                Arguments.of(Hit.of(Pins.of(9)), PinCount.of(PinCount.MIN_COUNT)),
                Arguments.of(Hit.of(Pins.of(2)), PinCount.of(PinCount.MIN_COUNT)),
                Arguments.of(Hit.of(Pins.of(PinCount.MIN_COUNT)), PinCount.of(PinCount.MIN_COUNT))
        );
    }

    @DisplayName("Hit 상태에 대한 문자열을 반환")
    @ParameterizedTest
    @MethodSource
    public void getDesc(final Hit hit, final String expected) {
        assertThat(hit.getDesc())
                .isEqualTo(expected);
    }

    private static Stream<Arguments> getDesc() {
        return Stream.of(
                Arguments.of(Hit.of(Pins.of(PinCount.MIN_COUNT)), StateExpression.GUTTER + StateExpression.BLANK),
                Arguments.of(Hit.of(Pins.of(2)), 2 + StateExpression.BLANK)
        );
    }
}
