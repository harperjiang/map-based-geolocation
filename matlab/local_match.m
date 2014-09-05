eg = 0.001; % sum-squared error goal
sc = 0.5;    % spread constant
nl = 50;   % neuron limit
rb_net = newrb(train,target,eg,sc,nl);
