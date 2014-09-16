function dy = backprop(t,y,e,param)
%MSE.BACKPROP

% Copyright 2012 The MathWorks, Inc.

disp('Customized Backprop');
dy = -2*e;
