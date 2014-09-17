function [rb_net, error] = train_rb(x, t, nlp)

if nargin < 3
    nlp = 1;
end

[m,n] = size(x);
eg = 0.0001; % sum-squared error goal
sc = 1;    % spread constant
%nl = 2000;   % neuron limit
nl = round(n*nlp);
rb_net = newrb(x,t,eg,sc,nl);

rb_output = rb_net(x);
error = gsubtract(rb_output, t);

end