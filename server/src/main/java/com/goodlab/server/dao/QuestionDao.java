package com.goodlab.server.dao;

import com.goodlab.server.model.Question;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class QuestionDao {

        // 模拟数据库存储
        private final Map<String, List<Question>> questionsByChapter = new HashMap<>();

        public QuestionDao() {
                initializeQuestions();
        }

        public List<Question> getQuestionsByChapter(String chapterId) {
                return questionsByChapter.getOrDefault(chapterId, new ArrayList<>());
        }

        public List<Question> getAllQuestions() {
                List<Question> allQuestions = new ArrayList<>();
                for (List<Question> questions : questionsByChapter.values()) {
                        allQuestions.addAll(questions);
                }
                return allQuestions;
        }

        public Question saveQuestion(Question question) {
                if (question.getId() == null) {
                        // 生成新ID
                        long maxId = getAllQuestions().stream()
                                        .mapToLong(Question::getId)
                                        .max()
                                        .orElse(0);
                        question.setId(maxId + 1);
                }

                String chapterId = question.getChapterId();
                List<Question> questions = questionsByChapter.getOrDefault(chapterId, new ArrayList<>());

                // 检查是否已存在相同ID的题目
                boolean found = false;
                for (int i = 0; i < questions.size(); i++) {
                        if (questions.get(i).getId().equals(question.getId())) {
                                questions.set(i, question);
                                found = true;
                                break;
                        }
                }

                if (!found) {
                        questions.add(question);
                }

                questionsByChapter.put(chapterId, questions);
                return question;
        }

        public boolean deleteQuestion(Long questionId) {
                for (List<Question> questions : questionsByChapter.values()) {
                        for (int i = 0; i < questions.size(); i++) {
                                if (questions.get(i).getId().equals(questionId)) {
                                        questions.remove(i);
                                        return true;
                                }
                        }
                }
                return false;
        }

        public Question getQuestionById(Long questionId) {
                for (List<Question> questions : questionsByChapter.values()) {
                        for (Question question : questions) {
                                if (question.getId().equals(questionId)) {
                                        return question;
                                }
                        }
                }
                return null;
        }

        public void importQuestions(List<Question> questions) {
                for (Question question : questions) {
                        saveQuestion(question);
                }
        }

        private void initializeQuestions() {
                // 第一章题目
                List<Question> chapter1Questions = Arrays.asList(
                                new Question(1L, "1", "choice", "人工智能的定义是什么？",
                                                "选择最准确的人工智能定义",
                                                Arrays.asList("能够执行特定任务的计算机程序", "模拟人类智能的计算机系统", "能够学习的算法", "自动化的机器"),
                                                1, 20, "人工智能是模拟人类智能的计算机系统，能够执行通常需要人类智能的任务。"),

                                new Question(2L, "1", "true-false", "图灵测试是判断机器是否具有智能的标准方法。", "",
                                                null, true, 15, "图灵测试由艾伦·图灵提出，通过判断机器能否在对话中让人类无法区分其是否为人类来测试智能。"),

                                new Question(3L, "1", "fill", "人工智能之父是谁？", "请输入人名",
                                                null, "约翰·麦卡锡", 25, "约翰·麦卡锡在1956年的达特茅斯会议上首次提出\"人工智能\"这一术语。"),

                                new Question(4L, "1", "choice", "以下哪个不是人工智能的主要应用领域？",
                                                "选择不属于AI主要应用领域的选项",
                                                Arrays.asList("自然语言处理", "计算机视觉", "数据库管理", "机器学习"),
                                                2, 20, "数据库管理是传统计算机科学领域，而其他三个都是AI的主要应用领域。"),

                                new Question(5L, "1", "true-false", "人工智能的发展经历了多次寒冬期。", "",
                                                null, true, 15, "AI发展确实经历了多次寒冬期，主要由于技术瓶颈和资金短缺。"));

                // 第二章题目
                List<Question> chapter2Questions = Arrays.asList(
                                new Question(6L, "2", "choice", "机器学习的核心是什么？",
                                                "选择机器学习最核心的概念",
                                                Arrays.asList("数据收集", "模式识别", "算法优化", "硬件升级"),
                                                1, 20, "机器学习的核心是通过数据识别模式，让计算机能够从经验中学习。"),

                                new Question(7L, "2", "choice", "监督学习的特点是什么？",
                                                "选择监督学习的正确描述",
                                                Arrays.asList("使用未标记的数据", "使用标记的训练数据", "通过奖励机制学习", "不需要数据"),
                                                1, 20, "监督学习使用标记的训练数据来训练模型，预测未知数据的输出。"),

                                new Question(8L, "2", "true-false", "无监督学习不需要标记数据。", "",
                                                null, true, 15, "无监督学习确实不需要标记数据，它通过发现数据中的隐藏模式来学习。"),

                                new Question(9L, "2", "fill", "线性回归用于预测什么类型的值？", "请输入答案",
                                                null, "连续值", 25, "线性回归用于预测连续值，如房价、温度等。"),

                                new Question(10L, "2", "choice", "以下哪个不是机器学习的主要类型？",
                                                "选择不属于机器学习主要类型的选项",
                                                Arrays.asList("监督学习", "无监督学习", "强化学习", "深度学习"),
                                                3, 20, "深度学习是机器学习的一个子集，不是主要类型。"));

                // 第三章题目
                List<Question> chapter3Questions = Arrays.asList(
                                new Question(11L, "3", "choice", "深度学习的核心是什么？",
                                                "选择深度学习的核心概念",
                                                Arrays.asList("多层神经网络", "大数据处理", "GPU加速", "算法优化"),
                                                0, 20, "深度学习的核心是多层神经网络，通过多层结构学习复杂的特征表示。"),

                                new Question(12L, "3", "true-false", "卷积神经网络主要用于图像处理。", "",
                                                null, true, 15, "CNN确实主要用于图像处理，能够有效提取图像的空间特征。"),

                                new Question(13L, "3", "fill", "反向传播算法用于什么？", "请输入答案",
                                                null, "更新权重", 25, "反向传播算法用于计算梯度并更新神经网络权重。"),

                                new Question(14L, "3", "choice", "以下哪个激活函数最常用？",
                                                "选择最常用的激活函数",
                                                Arrays.asList("Sigmoid", "Tanh", "ReLU", "Softmax"),
                                                2, 20, "ReLU是最常用的激活函数，因为它简单且有效。"),

                                new Question(15L, "3", "true-false", "过拟合是深度学习中的常见问题。", "",
                                                null, true, 15, "过拟合确实是深度学习中的常见问题，需要通过正则化等方法解决。"));

                // 第四章题目
                List<Question> chapter4Questions = Arrays.asList(
                                new Question(16L, "4", "choice", "AI在医疗领域的主要应用不包括？",
                                                "选择不属于AI医疗应用的选项",
                                                Arrays.asList("医学影像诊断", "药物发现", "患者护理", "硬件制造"),
                                                3, 20, "硬件制造不属于AI医疗应用，其他都是AI在医疗领域的重要应用。"),

                                new Question(17L, "4", "true-false", "自动驾驶汽车使用多种AI技术。", "",
                                                null, true, 15, "自动驾驶汽车确实使用计算机视觉、机器学习等多种AI技术。"),

                                new Question(18L, "4", "fill", "智能语音助手主要使用什么技术？", "请输入答案",
                                                null, "自然语言处理", 25, "智能语音助手主要使用自然语言处理技术来理解和生成人类语言。"),

                                new Question(19L, "4", "choice", "AI在金融领域的主要应用是？",
                                                "选择AI在金融领域的主要应用",
                                                Arrays.asList("风险评估", "客户服务", "投资决策", "以上都是"),
                                                3, 20, "AI在金融领域的应用包括风险评估、客户服务、投资决策等多个方面。"),

                                new Question(20L, "4", "true-false", "AI可以用于环境保护。", "",
                                                null, true, 15, "AI确实可以用于环境保护，如监测污染、预测气候变化等。"));

                // 第五章题目
                List<Question> chapter5Questions = Arrays.asList(
                                new Question(21L, "5", "choice", "AI发展的主要趋势不包括？",
                                                "选择不属于AI发展趋势的选项",
                                                Arrays.asList("更强大的计算能力", "更好的算法", "更少的数据需求", "更简单的模型"),
                                                3, 20, "AI发展趋势是更复杂的模型，而不是更简单的模型。"),

                                new Question(22L, "5", "true-false", "边缘计算是AI发展的重要方向。", "",
                                                null, true, 15, "边缘计算确实是AI发展的重要方向，可以减少延迟和隐私问题。"),

                                new Question(23L, "5", "fill", "大语言模型的主要特点是什么？", "请输入答案",
                                                null, "大规模参数", 25, "大语言模型的主要特点是拥有大规模参数，能够处理复杂的语言任务。"),

                                new Question(24L, "5", "choice", "AI伦理的核心问题包括？",
                                                "选择AI伦理的核心问题",
                                                Arrays.asList("隐私保护", "算法偏见", "就业影响", "以上都是"),
                                                3, 20, "AI伦理的核心问题包括隐私保护、算法偏见、就业影响等多个方面。"),

                                new Question(25L, "5", "true-false", "AI的发展需要多学科合作。", "",
                                                null, true, 15, "AI的发展确实需要计算机科学、数学、心理学等多学科合作。"));

                // 第六章题目
                List<Question> chapter6Questions = Arrays.asList(
                                new Question(26L, "6", "choice", "Python在AI开发中的优势不包括？",
                                                "选择不属于Python优势的选项",
                                                Arrays.asList("丰富的库", "易学易用", "执行速度快", "社区支持"),
                                                2, 20, "Python的执行速度相对较慢，这是它的劣势之一。"),

                                new Question(27L, "6", "true-false", "TensorFlow是深度学习框架。", "",
                                                null, true, 15, "TensorFlow确实是Google开发的深度学习框架。"),

                                new Question(28L, "6", "fill", "数据预处理的主要目的是什么？", "请输入答案",
                                                null, "提高模型性能", 25, "数据预处理的主要目的是提高模型性能和训练效率。"),

                                new Question(29L, "6", "choice", "模型评估的常用指标不包括？",
                                                "选择不属于模型评估指标的选项",
                                                Arrays.asList("准确率", "精确率", "召回率", "计算速度"),
                                                3, 20, "计算速度不是模型评估指标，其他都是常用的评估指标。"),

                                new Question(30L, "6", "true-false", "交叉验证可以提高模型的泛化能力。", "",
                                                null, true, 15, "交叉验证确实可以提高模型的泛化能力，减少过拟合。"));

                // 第七章题目
                List<Question> chapter7Questions = Arrays.asList(
                                new Question(31L, "7", "choice", "AI伦理的基本原则不包括？",
                                                "选择不属于AI伦理基本原则的选项",
                                                Arrays.asList("公平性", "透明度", "责任性", "效率性"),
                                                3, 20, "效率性不是AI伦理的基本原则，其他都是重要的伦理原则。"),

                                new Question(32L, "7", "true-false", "AI可能加剧社会不平等。", "",
                                                null, true, 15, "AI确实可能加剧社会不平等，这是需要关注的重要问题。"),

                                new Question(33L, "7", "fill", "AI安全的核心问题是什么？", "请输入答案",
                                                null, "控制问题", 25, "AI安全的核心问题是控制问题，即如何确保AI系统按照人类意图行动。"),

                                new Question(34L, "7", "choice", "AI的未来发展方向包括？",
                                                "选择AI的未来发展方向",
                                                Arrays.asList("通用人工智能", "人机协作", "可持续发展", "以上都是"),
                                                3, 20, "AI的未来发展方向包括通用人工智能、人机协作、可持续发展等多个方面。"),

                                new Question(35L, "7", "true-false", "AI的发展需要全球合作。", "",
                                                null, true, 15, "AI的发展确实需要全球合作，共同应对挑战和机遇。"));

                questionsByChapter.put("1", chapter1Questions);
                questionsByChapter.put("2", chapter2Questions);
                questionsByChapter.put("3", chapter3Questions);
                questionsByChapter.put("4", chapter4Questions);
                questionsByChapter.put("5", chapter5Questions);
                questionsByChapter.put("6", chapter6Questions);
                questionsByChapter.put("7", chapter7Questions);
        }
}