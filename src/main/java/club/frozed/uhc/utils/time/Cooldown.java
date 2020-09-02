package club.frozed.uhc.utils.time;

import java.util.UUID;

public class Cooldown {
    private UUID uniqueId;
    private long start;
    private long expire;
    private boolean notified;

    public Cooldown(final long duration) {
        this.uniqueId = UUID.randomUUID();
        this.start = System.currentTimeMillis();
        this.expire = this.start + duration;
        if (duration == 0L) {
            this.notified = true;
        }
    }

    public long getPassed() {
        return System.currentTimeMillis() - this.start;
    }

    public long getRemaining() {
        return this.expire - System.currentTimeMillis();
    }

    public boolean hasExpired() {
        return System.currentTimeMillis() - this.expire >= 0L;
    }

    public String getTimeLeft() {
        if (this.getRemaining() >= 60000L) {
            return TimeUtil.millisToRoundedTime(this.getRemaining());
        }
        return TimeUtil.millisToSeconds(this.getRemaining());
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public long getStart() {
        return this.start;
    }

    public long getExpire() {
        return this.expire;
    }

    public boolean isNotified() {
        return this.notified;
    }

    public void setUniqueId(final UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void setStart(final long start) {
        this.start = start;
    }

    public void setExpire(final long expire) {
        this.expire = expire;
    }

    public void setNotified(final boolean notified) {
        this.notified = notified;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Cooldown)) {
            return false;
        }
        final Cooldown other = (Cooldown)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$uniqueId = this.getUniqueId();
        final Object other$uniqueId = other.getUniqueId();
        if (this$uniqueId == null) {
            if (other$uniqueId == null) {
                return this.getStart() == other.getStart() && this.getExpire() == other.getExpire() && this.isNotified() == other.isNotified();
            }
        }
        else if (this$uniqueId.equals(other$uniqueId)) {
            return this.getStart() == other.getStart() && this.getExpire() == other.getExpire() && this.isNotified() == other.isNotified();
        }
        return false;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Cooldown;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $uniqueId = this.getUniqueId();
        result = result * 59 + (($uniqueId == null) ? 43 : $uniqueId.hashCode());
        final long $start = this.getStart();
        result = result * 59 + (int)($start >>> 32 ^ $start);
        final long $expire = this.getExpire();
        result = result * 59 + (int)($expire >>> 32 ^ $expire);
        result = result * 59 + (this.isNotified() ? 79 : 97);
        return result;
    }

    @Override
    public String toString() {
        return "Cooldown(uniqueId=" + this.getUniqueId() + ", start=" + this.getStart() + ", expire=" + this.getExpire() + ", notified=" + this.isNotified() + ")";
    }
}

