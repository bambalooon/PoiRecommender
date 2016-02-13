package pl.edu.agh.eis.poirecommender.utils;

import com.google.common.base.Optional;
import lombok.Value;

@Value
public class AsyncResult<T> {
    private final Optional<? extends T> data;
    private final Optional<Integer> messageResource;
}
