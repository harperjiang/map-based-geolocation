function [rb_net, error] = train_rb(x, t)


[m,n] = size(x);
eg = 0.0001; % sum-squared error goal
sc = 1;    % spread constant
%nl = 2000;   % neuron limit
nl = n;
rb_net = newrb(x,t,eg,sc,nl);

rb_output = rb_net(x);
error = gsubtract(rb_output, t);

end