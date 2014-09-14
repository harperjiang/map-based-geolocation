% For each point, look for a dataset surrounding it
clear;
load('input.mat');
dist = 3;

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
            round_input = cat(2, round_input, input(:,sindex));
            round_target = cat(2, round_target, target(:,sindex));
        end
    end
    
    [m,n] = size(round_input);
    
    data_size(1,index) = n;
    if n == 0
       continue; 
    end
    net = best_nnet(round_input, round_target,30);
    err = norm(net(data)-center);
    errors(1,index) = err;
end