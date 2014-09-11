function [output] = pnnet(nets, input)
    % Output the average result from parallel mlp
    sz = length(nets);
    
    outputs = [];
    
    for i = 1:sz 
        outputs = cat(3, outputs,nets{i}(input));
    end
    
    % Calculate the median
    [m,n,k] = size(outputs);
    
    output = zeros([m,n]);
    for i = 1:m
        for j = 1:n
            output(i,j) = median(outputs(i,j,:));
        end
    end
    return;
end