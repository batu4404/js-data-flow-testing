(declare-fun a_s () Real)
(declare-fun b_s () Real)
(assert (not (= (+ (+ a_s b_s) b_s) b_s)) )
(assert (not (= (+ (+ a_s b_s) b_s) (+ (+ a_s b_s) b_s))) )
(check-sat)
(get-model)