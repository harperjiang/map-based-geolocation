% For each point, look for a dataset surrounding it

dist = 2;
threshold = 20;
for index = 1:1547
    
    center = target(:,index);
    
    % Collect data around the center point
    round_input = [];
    round_target = [];
    
    for sindex = 1:1547
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
    if n < threshold 
        continue;
    end
    
    % Train neural network around the center point
    

end