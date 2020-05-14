package org.itstack.demo.bytebuddy;

public abstract class Repository<T> {

    public abstract T queryData(int id);

}
