function [ beta, errs ] = train_volterra( x, t )
%VOLTERRA_FIT Summary of this function goes here
%   Quadratic model

    [xm,n] = size(x);
    [tm,n] = size(t);

    x2 = [];
    for i = 1 : xm
        for j = i : xm
            x2 = [x2; x(i,:).*x(j,:)];
        end
    end
    
    xin = [ones([1,n]);x;x2];
    
    A = xin';
    b = t';
        
    beta = (A'*A)\A'*b;
    
    predict = (A * beta)';
    
    errs = t - predict;
    return;
end

