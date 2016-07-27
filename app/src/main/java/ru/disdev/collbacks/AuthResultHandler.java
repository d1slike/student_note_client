package ru.disdev.collbacks;

import java.io.Serializable;

/**
 * Created by DisDev on 23.07.2016.
 */
public interface AuthResultHandler extends Serializable {

    void onSuccess();
    void onError(int errorCode);

}
