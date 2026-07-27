package io.github.lazyimmortal.sesame.model.extensions.messagePush;

import java.util.LinkedHashSet;

import io.github.lazyimmortal.sesame.data.Model;
import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.data.ModelGroup;
import io.github.lazyimmortal.sesame.data.extensions.ExtensionsConfig;
import io.github.lazyimmortal.sesame.data.modelFieldExt.EmptyModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.StringModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.TextModelField;
import io.github.lazyimmortal.sesame.entity.idAndName.CustomOption;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.Status;
import io.github.lazyimmortal.sesame.util.StringUtil;
import io.github.lazyimmortal.sesame.util.ThreadUtil;
import io.github.lazyimmortal.sesame.util.ToastUtil;

public class MessagePush extends Model {
    @Override
    public String getName() {
        return "消息推送";
    }

    @Override
    public ModelGroup getGroup() {
        return ModelGroup.OTHER;
    }

    private static final SelectModelField messagePushTypes = new SelectModelField("messagePushTypes", "消息推送 | 类型列表", new LinkedHashSet<>(), MessagePushType.class);
    private static final SelectModelField messagePushChannels = new SelectModelField("messagePushChannels", "消息推送 | 频道列表", new LinkedHashSet<>(), MessagePushChannel.class);
    private static final StringModelField setTelegramBotToken = new StringModelField("setTelegramBotToken", "TelegramBot | 设置BotToken", "");
    private static final StringModelField setTelegramChatId = new StringModelField("setTelegramChatId", "TelegramBot | 设置ChatId", "");
    private static final StringModelField setWxPusherPushAppToken = new StringModelField("setWxPusherPushAppToken", "WxPusher | 设置标准推送AppToken", "");
    private static final StringModelField setWxPusherPushUID = new StringModelField("setWxPusherPushUID", "WxPusher | 设置标准推送UID", "");
    private static final TextModelField.UrlTextModelField getWxPusherSimplePushToken = new TextModelField.UrlTextModelField("getWxPusherSimplePushToken", "WxPusher | 获取极简推送Token", "https://wxpusher.zjiecode.com/api/qrcode/RwjGLMOPTYp35zSYQr0HxbCPrV9eU0wKVBXU1D5VVtya0cQXEJWPjqBdW3gKLifS.jpg");
    private static final StringModelField setWxPusherSimplePushToken = new StringModelField("setWxPusherSimplePushToken", "WxPusher | 设置极简推送Token", "", "微信扫描上面的二维码获取Token");

    @Override
    public ModelFields getFields() {
        ModelFields modelFields = new ModelFields();
        modelFields.addField(messagePushTypes);
        modelFields.addField(messagePushChannels);
        modelFields.addField(setTelegramBotToken);
        modelFields.addField(setTelegramChatId);
        modelFields.addField(setWxPusherPushAppToken);
        modelFields.addField(setWxPusherPushUID);
        modelFields.addField(getWxPusherSimplePushToken);
        modelFields.addField(setWxPusherSimplePushToken);
        modelFields.addField(new EmptyModelField("sendTestMessage", "测试消息推送",
                (c, m) -> ThreadUtil.start(() -> {
                    try {
                        sendMessage("这是一条测试消息", true);
                        ToastUtil.show(c, "测试消息已推送，请在相关平台查看推送结果");
                    } catch (Exception e) {
                        Log.printStackTrace(e);
                    }
                }),
                "确认推送测试消息？")
        );
        return modelFields;
    }

    /**
     * 发送测试推送
     *
     * @param channel 推送频道
     * @param content 推送内容
     */
    public static void sendMessage(MessagePushChannel channel, String content) {
        ExtensionsConfig.load();
        if (messagePushChannels.contains(channel.name())) {
            sendMessage(content, false);
        }
    }

    /**
     * 发送推送消息
     *
     * @param content 推送内容
     * @param force   是否强制推送
     */
    public static void sendMessage(String content, boolean force) {
        do {
            if (!messagePushTypes.contains(MessagePushType.TelegramBot.name())) {
                break;
            }
            if (!force && Status.hasFlagToday(MessagePushFlag.TelegramBot.flagName(content))) {
                break;
            }
            String botToken = setTelegramBotToken.getValue();
            String chatId = setTelegramChatId.getValue();
            if (StringUtil.hasEmpty(botToken, chatId)) {
                break;
            }
            if (TelegramBotManger.sendMessage(botToken, chatId, content) && !force) {
                Status.flagToday(MessagePushFlag.TelegramBot.flagName(content));
            }
        } while (false);
        do {
            if (!messagePushTypes.contains(MessagePushType.WxPusherPush.name())) {
                break;
            }
            if (!force && Status.hasFlagToday(MessagePushFlag.WxPusherPush.flagName(content))) {
                break;
            }
            String appToken = setWxPusherPushAppToken.getValue();
            String uid = setWxPusherPushUID.getValue();
            if (StringUtil.hasEmpty(appToken, uid)) {
                break;
            }
            String summary = StringUtil.getSubString(content, ">>", "");
            if (WxPusherManager.sendPush(appToken, uid, summary, content) && !force) {
                Status.flagToday(MessagePushFlag.WxPusherPush.flagName(content));
            }
        } while (false);
        do {
            if (!messagePushTypes.contains(MessagePushType.WxPusherSimplePush.name())) {
                break;
            }
            if (!force && Status.hasFlagToday(MessagePushFlag.WxPusherSimplePush.flagName(content))) {
                break;
            }
            String simplePushToken = setWxPusherSimplePushToken.getValue();
            if (StringUtil.isEmpty(simplePushToken)) {
                break;
            }
            if (WxPusherManager.sendSimplePush(simplePushToken, content) && !force) {
                Status.flagToday(MessagePushFlag.WxPusherSimplePush.flagName(content));
            }
        } while (false);
    }

    public enum MessagePushType implements CustomOption {
        TelegramBot("TelegramBot(支付宝需要能跳过高墙)"),
        WxPusherPush("WxPusher | 标准推送"),
        WxPusherSimplePush("WxPusher | 极简推送");

        private final String nickName;

        MessagePushType(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }

    public enum MessagePushChannel implements CustomOption {
        SPECIAL_EVENT("特殊事件"),
        ANT_ORCHARD("芭芭农场(果树可以兑换了)"),
        PROTECT_ECOLOGY("生态保护(保护地、海洋列表上新)"),
        REQUEST_ERROR("请求异常");


        private final String nickName;

        MessagePushChannel(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }

    private enum MessagePushFlag implements Status.StatusFlag {
        TelegramBot,
        WxPusherPush,
        WxPusherSimplePush
    }
}