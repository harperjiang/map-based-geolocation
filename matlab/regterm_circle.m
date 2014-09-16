function perf = regterm_circle( t, y, e )
%REGTERM_CIRCLE Summary of this function goes here
%   Regularization Term of Circle

% Global Parameters to control the regularization term
%global reg_center;
global reg_radius;
global reg_weight;

% Slope of sigmoid function
lambda = 1000;

[m,n] = size(e);
perf = zeros([1,n]);

for i = 1 : n
    ie = e(:,i);
    if(isnan(ie(1))==1 || isnan(ie(2))==1)
        perf(i) = 0;
    else
        perf(i) = reg_weight * sigmf(norm(ie),[lambda reg_radius]);
    end
end

end

