package com.matfragg.shopping_car.api.shared.security.aspect;

import com.matfragg.shopping_car.api.shared.security.SecurityUtils;
import com.matfragg.shopping_car.api.shared.security.annotation.CurrentUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


@Component
@RequiredArgsConstructor
public class CurrentUserIdResolver implements HandlerMethodArgumentResolver {
    private final SecurityUtils securityUtils;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUserId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        return securityUtils.getCurrentUserId();
    }
}