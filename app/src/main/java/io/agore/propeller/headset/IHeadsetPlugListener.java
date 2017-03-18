package io.agore.propeller.headset;

public interface IHeadsetPlugListener {
    public void notifyHeadsetPlugged(boolean plugged, Object... extraData);
}
