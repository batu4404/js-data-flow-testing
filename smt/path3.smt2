(declare-fun a_s () Real)
(declare-fun b_s () Real)
(assert (= (+ (+ a_s b_s) b_s) b_s) )
(assert (!= b_s b_s) )
(check-sat)
(get-model)