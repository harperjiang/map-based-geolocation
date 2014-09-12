function [output] = best_nnet(input, target, ite)
    % Output the nnet with minimal median error
    
    best_median = Inf;
    
    output = zeros([m,n]);
    for i = 1:m
        for j = 1:n
            output(i,j) = median(outputs(i,j,:));
        end
    end
    return;
end