package com.linketinder.service

interface IBaseService<T> {

    List<T> getAll()
    T getById(Integer id)
    void add(T t)
    void update(Integer id, T t)
    void delete(Integer id)

}
