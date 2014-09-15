function [ beta, errs ] = train_linear( x, t )
%VOLTERRA_FIT Summary of this function goes here
%   Quadratic model

    [xm,n] = size(x);
    [tm,n] = size(t);
    
    xin = [ones([1,n]);x];
    
    A = xin';
    b = t';
        
    beta = (A'*A)\A'*b;
    
    predict = (A * beta)';
    
    errs = t - predict;
    return;
end

