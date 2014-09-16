function perfs = apply(t,y,e,param)
%MSE.APPLY

% Copyright 2012 The MathWorks, Inc.
%disp('Customized Apply');
perfs = e .* e;
perfs = gadd(perfs,regterm_circle(t,y,e));
