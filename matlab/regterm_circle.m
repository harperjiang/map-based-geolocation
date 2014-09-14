function perf = regterm_circle( y, t )
%REGTERM_CIRCLE Summary of this function goes here
%   Regularization Term of Circle

% Global Parameters to control the regularization term
global reg_center;
global reg_radius;
global reg_weight;

% Slope of sigmoid function
lambda = 100;

perf = reg_weight * sigmf(norm(y-reg_center),[lambda reg_radius]);

end

