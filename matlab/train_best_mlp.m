function net = train_best_mlp(input, target, ite)
    % Output the nnet with minimal median error
    
    best_perf = Inf;
    
    for i = 1 : ite
        [current_net, errors, current_perf] = train_mlp(input,target);    
        if current_perf < best_perf
            net = current_net;
            best_perf = current_perf;
        end
    end
end