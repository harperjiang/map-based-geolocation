% For each point, look for a dataset surrounding it
load('input.mat');
dist = 3;
threshold = 20;

[tm,tn] = size(input);

errors = zeros([1,tn]);
data_size = zeros([1,tn]);

for index = 1:tn
    data = input(:,index);
    center = target(:,index);
    
    % Collect data around the center point
    round_input = [];
    round_target = [];
    
    for sindex = 1:tn
        if sindex == index
            continue;
        end
        
        current = target(:,sindex);
        
        if norm(current - center) <= dist
            cat(2, round_input, input(:,sindex));
            cat(2, round_target, target(:,sindex));
        end
    end
    
    % Ignore input with to less data
    [m,n] = size(round_input);
    data_size(1,index) = n;
    net = best_nnet(round_input, round_target);
    err = norm(net(data)-center);
    errors(1,index) = err;
end