input = train_input';
target = train_target';

eg = 4; % sum-squared error goal
sc = 100;    % spread constant
nl = 2000;   % neuron limit
rb_net = newrb(input,target,eg,sc,nl);

rb_output = rb_net(input);
rb_error = gsubtract(rb_output, target);