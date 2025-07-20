package com.dongpv.sns.identity.service;

import java.util.List;

public interface IGenericService<I, C, U, D> {
    D create(C request);

    D update(I id, U request);

    void delete(I[] ids);

    List<D> getAll();

    D getById(I id);
}
