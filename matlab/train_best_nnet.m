function net = nnet_best_net(input, target, ite)
    % Output the nnet with minimal median error
    
    best_perf = Inf;
    
    for i = 1 : ite
        [current_net, errors, current_perf] = train_single_nnet(input,target);    
        if current_perf < best_perf
            net = current_net;
        end
    end
end