package bowling.domain;

public class NormalFrame extends Frame {

    public static final int FIRST_NUMBER = 1;
    private static final int LAST_NUMBER = 9;

    private Frame next;

    private NormalFrame(int number) {
        super(number);
    }

    private NormalFrame(int number, Pitches pitches) {
        super(number, pitches);
    }

    public static NormalFrame first() {
        return new NormalFrame(FIRST_NUMBER, new Pitches());
    }

    @Override
    public Frame next() {
        validateCreateNextFrame();
        if (hasNext()) {
            return this.next;
        }
        return createNextFrame();
    }

    private void validateCreateNextFrame() {
        if (!isFinished()) {
            throw new IllegalStateException("종료되지 않은 프레임입니다. 다음 프레임을 시작할 수 없습니다.");
        }
    }

    private Frame createNextFrame() {
        this.next = createNextFrame(isLast());
        return this.next;
    }

    private Frame createNextFrame(boolean isLast) {
        if (isLast) {
            return new FinalFrame(number() + 1);
        }
        return new NormalFrame(number() + 1);
    }

    private boolean isLast() {
        return number() == LAST_NUMBER;
    }

    private boolean hasNext() {
        return this.next != null;
    }

    @Override
    public void pitch(Pitch pitch) {
        pitches().add(pitch);
        if (pitch.isStrike()) {
            pitches().decreasePitchAbleCount();
        }
    }

    @Override
    public boolean isFinished() {
        return pitches().isFinished();
    }

    @Override
    public int score() {
        return pitches().pinDownCount() + bonusScore();
    }

    private int bonusScore() {
        if (hasNext()) {
            return next.bonusScore(pitches());
        }
        return NON_BONUS;
    }

    @Override
    public int bonusScore(Pitches beforePitches) {
        if (isDoubleStrike(beforePitches)) {
            return pitches().pinDownCount() + doubleBonusScore();
        }
        if (beforePitches.isStrike()) {
            return pitches().pinDownCount();
        }
        if (beforePitches.isSpare()) {
            return pitches().firstPinDownCount();
        }
        return NON_BONUS;
    }

    private boolean isDoubleStrike(Pitches beforePitches) {
        return beforePitches.isStrike() && pitches().isStrike();
    }

    @Override
    public int doubleBonusScore() {
        if (hasNext()) {
            Pitches nextFramePitches = next.pitches();
            return nextFramePitches.firstPinDownCount();
        }
        return NON_BONUS;
    }

    @Override
    public boolean isScoreDecided() {
        if (pitches().isStrike() || pitches().isSpare()) {
            return isBonusScoreDecided();
        }
        return pitches().isFinished();
    }

    private boolean isBonusScoreDecided() {
        if (hasNext()) {
            return next.isBonusScoreDecided(pitches());
        }
        return false;
    }

    @Override
    public boolean isBonusScoreDecided(Pitches beforePitches) {
        if (isDoubleStrike(beforePitches)) {
            return hasNext();
        }
        if (beforePitches.isStrike()) {
            return pitches().isFinished();
        }
        if (beforePitches.isSpare()) {
            return !pitches().isEmpty();
        }
        return false;
    }

}
