function error = train_rb(x, t, nlp, sc)

if nargin < 3
    nlp = 1;
end
if nargin < 4
    sc = 10
end

[m,n] = size(x);
[w,n] = size(t);
eg = 0.0001; % sum-squared error goal    %nl = 2000;   % neuron limit
nl = round(n*nlp);

error = zeros([w,n]);

for i = 1 : n
    if i == 1 
        x_prime = x(:,1);
        t_prime = t(:,1);
    else if i == n
            x_prime = x(:,n);
            t_prime = t(:,n);
        else
            x_prime = x(:,[1:i-1,i+1:n]);
            t_prime = t(:,[1:i-1,i+1:n]);
        end
    end
    rb_net = newrb(x_prime,t_prime,eg,sc,nl);
    rb_output = rb_net(x(:,i));
    error(:,i) = gsubtract(rb_output, t(:,i));
end

end